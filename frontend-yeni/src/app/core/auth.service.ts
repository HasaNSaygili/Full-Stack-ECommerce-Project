import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  login(email: string, password: string): Observable<{ token: string }> {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { email, password });
  }

  register(user: any) {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  setToken(token: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('token', token);
    }
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    }
    return null;
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return isPlatformBrowser(this.platformId) && this.validateToken();
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role;
    } catch {
      return null;
    }
  }

  hasRole(role: string): boolean {
    const userRole = this.getUserRole();
    return userRole === role || userRole === `ROLE_${role}`;
  }

  getCurrentUser(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        email: payload.sub,
        role: payload.role,
        id: +payload.userId
      };
    } catch {
      return null;
    }
  }

  getProfile() {
    const token = this.getToken();
    return this.http.get<any>(`${this.apiUrl}/me`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  validateToken(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Math.floor(Date.now() / 1000);
      return payload.exp > now;
    } catch {
      return false;
    }
  }
}
