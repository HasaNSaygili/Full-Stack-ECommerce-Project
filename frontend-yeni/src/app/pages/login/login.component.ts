import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [ Validators.required, Validators.email ]],
      password: ['', [ Validators.required, Validators.minLength(6) ]]
    });
  }

  login(): void {
    if (this.loginForm.invalid) {
      this.toastr.error('Lütfen geçerli bilgiler girin.');
      return;
    }

    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: (res) => {
        this.authService.setToken(res.token);
        this.toastr.success('Giriş Başarılı!');
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.toastr.error('Giriş Başarısız!');
        console.error('Login error:', err.status, err.error);

      }
    });
  }
}
