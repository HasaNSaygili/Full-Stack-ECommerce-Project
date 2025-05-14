import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }

  deleteUser(id: number): Observable<any> {
  return this.http.delete(`/api/admin/delete-user/${id}`);
  }


  changeUserRole(id: number, role: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/role`, { role });

  }
}
