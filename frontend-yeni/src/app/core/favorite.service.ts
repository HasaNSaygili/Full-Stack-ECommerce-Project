import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private apiUrl = `${environment.apiUrl}/favorites`;

  constructor(private http: HttpClient) {}

  addFavorite(userId: number, productId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${userId}/${productId}`, {});
  }

  removeFavorite(userId: number, productId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${userId}/${productId}`);
  }

  getFavorites(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${userId}`);
  }

  isFavorite(userId: number, productId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/user/${userId}/product/${productId}`);
  }
}
