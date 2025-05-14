import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { StripeService, StripeCardComponent } from 'ngx-stripe';
import type { StripeCardElementOptions, StripeElementsOptions } from '@stripe/stripe-js';

import { CartService } from '../../core/cart.service';
import { OrderService } from '../../core/order.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../core/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  @ViewChild(StripeCardComponent) card!: StripeCardComponent;

  cartItems: any[] = [];
  total = 0;
  loading = true;

  shippingAddress = '';
  billingAddress = '';
  paymentMethod = 'Kredi Kartı';

  cardError: string | null = null;

  cardOptions: StripeCardElementOptions = {
    style: {
      base: {
        fontSize: '16px',
        color: '#32325d',
        '::placeholder': { color: '#a0aec0' }
      }
    }
  };
  elementsOptions: StripeElementsOptions = {
    locale: 'tr'
  };

  constructor(
    private stripeService: StripeService,
    private cartService: CartService,
    private orderService: OrderService,
    private toastr: ToastrService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.toastr.error('Giriş yapmalısınız.');
      return;
    }

    this.cartService.getCart(user.id).subscribe({
      next: cart => {
        this.cartItems = cart.cartItems;
        this.total = cart.totalAmount;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Sepet alınamadı.');
        this.loading = false;
      }
    });
  }

  pay(): void {
    if (!this.shippingAddress || !this.billingAddress) {
      this.toastr.error('Lütfen tüm alanları doldurun.');
      return;
    }

    if (this.paymentMethod === 'Kredi Kartı') {
      this.loading = true;
      this.cardError = null;

      if (!this.card || !this.card.element) {
        this.toastr.error('Kart alanı henüz yüklenmedi. Lütfen sayfayı yenileyin.');
        this.loading = false;
        return;
      }

      const amountInKurus = Math.round(this.total * 100);

      this.http
        .post(
          `/api/payments/create-payment-intent?amount=${amountInKurus}`,
          {},
          { responseType: 'text' }
        )
        .subscribe({
          next: (clientSecret: string) => {
            this.stripeService
              .confirmCardPayment(clientSecret, {
                payment_method: {
                  card: this.card.element,
                  billing_details: {
                    name: this.authService.getCurrentUser().fullName || 'Test User',
                    email: this.authService.getCurrentUser().email,
                    address: { postal_code: '34000' }
                  }
                }
              })
              .subscribe(result => {
                if (result.error) {
                  this.cardError = result.error.message || null;
                  this.loading = false;
                } else if (
                  result.paymentIntent?.status === 'succeeded' ||
                  result.paymentIntent?.status === 'processing'
                ) {
                  const paymentIntentId = result.paymentIntent?.id;
                  this.createOrder(paymentIntentId);
                } else {
                  this.cardError = 'Ödeme tamamlanamadı: ' + result.paymentIntent?.status;
                  this.loading = false;
                }
              });
          },
          error: err => {
            console.error('☹️ create-payment-intent hatası:', err);
            this.toastr.error(
              typeof err.error === 'string' ? err.error : 'Ödeme oluşturulamadı.'
            );
            this.loading = false;
          }
        });
    } else {
      this.createOrder(); // Kapıda ödeme
    }
  }

  private createOrder(stripeTransactionId?: string): void {
    const user = this.authService.getCurrentUser()!;
    const orderItems = this.cartItems.map(item => ({
      productId: item.productId ?? item.id ?? item.product?.id,
      quantity: item.quantity
    }));

    const orderRequest: any = {
      items: orderItems,
      addressId: 1,
      shippingAddress: this.shippingAddress,
      billingAddress: this.billingAddress,
      paymentMethod: this.paymentMethod,
      cardNumber: null,
      transactionId: stripeTransactionId ?? null,
      amount: this.total
    };

    this.orderService.placeOrder(user.id, orderRequest).subscribe({
      next: () => {
        this.toastr.success('Sipariş oluşturuldu!');
        this.cartService.clearCart(user.id).subscribe(() => {
          this.router.navigate(['/orders']);
        });
      },
      error: () => {
        this.toastr.error('Sipariş oluşturulamadı.');
      }
    });
  }
}
