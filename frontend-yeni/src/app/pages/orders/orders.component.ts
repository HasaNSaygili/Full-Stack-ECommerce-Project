import { Component } from '@angular/core';
import { OrderService } from '../../core/order.service';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-orders',
  standalone: false,
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent {
  orders: any[] = [];
  userId!: number;

  constructor(private orderService: OrderService, private authService: AuthService) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
      this.orderService.getOrders(this.userId).subscribe(data => {
        this.orders = data;
      });
    }
  }
}
