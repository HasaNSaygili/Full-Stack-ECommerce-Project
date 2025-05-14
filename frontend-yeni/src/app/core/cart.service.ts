import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = `${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) {}

  getCart(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${userId}`);
  }

  addToCart(userId: number, productId: number, quantity: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${userId}/add`, { productId, quantity });
  }

  removeFromCart(userId: number, productId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${userId}/remove/${productId}`);
  }

  clearCart(userId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${userId}/clear`);
  }
}
