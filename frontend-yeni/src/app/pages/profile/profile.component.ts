import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.getProfile().subscribe({
        next: (data) => {
          this.user = data;
        },
        error: (err) => {
          console.error('Kullanıcı bilgisi alınamadı:', err);
          this.errorMessage = 'Profil bilgisi alınamadı veya yetkiniz yok.';
        }
      });
    } else {
      this.errorMessage = 'Giriş yapılmamış.';
    }
  }
}
