import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../core/cart.service';
import { AuthService } from '../../core/auth.service';
import { OrderService } from '../../core/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: any;
  total: number = 0;
  userId!: number;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
      this.loadCart();
    }
  }

  loadCart() {
    this.cartService.getCart(this.userId).subscribe(data => {
      this.cart = data;
      this.total = data.totalAmount;
    });
  }

  increaseQuantity(productId: number) {
    this.cartService.addToCart(this.userId, productId, 1).subscribe(() => this.loadCart());
  }

  decreaseQuantity(productId: number, currentQuantity: number) {
    if (currentQuantity <= 1) {
      this.removeItem(productId);
    } else {
      this.cartService.addToCart(this.userId, productId, -1).subscribe(() => this.loadCart());
    }
  }

  removeItem(productId: number) {
    this.cartService.removeFromCart(this.userId, productId).subscribe({
      next: () => this.loadCart(),
      error: err => console.error('Silme hatası:', err)
    });
  }

  clearCart() {
    this.cartService.clearCart(this.userId).subscribe({
      next: () => this.loadCart(),
      error: err => console.error('Temizleme hatası:', err)
    });
  }

  completeOrder() {
    this.router.navigate(['/checkout']);
  }

}
