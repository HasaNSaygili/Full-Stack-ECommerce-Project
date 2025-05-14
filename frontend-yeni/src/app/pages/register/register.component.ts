import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      role: ['ROLE_USER', Validators.required],
      storeName: ['']
    });
  }

  register() {
    if (this.registerForm.valid) {
      const {
        username,
        email,
        password,
        confirmPassword,
        role,
        storeName
      } = this.registerForm.value;

      if (password !== confirmPassword) {
        this.toastr.error('Şifreler uyuşmuyor!');
        return;
      }

      const newUser = {
        username,
        email,
        password,
        role,
        storeName: role === 'ROLE_SELLER' ? storeName : null
      };

      this.authService.register(newUser).subscribe({
        next: () => {
          this.toastr.success('Kayıt Başarılı!');
          this.router.navigate(['/login']);
        },
        error: () => {
          this.toastr.error('Kayıt başarısız!');
        }
      });
    } else {
      this.toastr.error('Lütfen tüm alanları doğru doldurun.');
    }
  }
}
