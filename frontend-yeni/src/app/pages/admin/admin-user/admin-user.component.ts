import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../core/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css'],
  standalone: false
})
export class AdminUserComponent implements OnInit {
  users: any[] = [];

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
    });
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.loadUsers();
        this.toastr.success('Kullanıcı silindi');
      },
      error: (err: HttpErrorResponse) => {
        const errorMsg = typeof err?.error === 'string'
          ? err.error
          : err?.error?.message || 'Kullanıcı silinemedi!';
        this.toastr.error(errorMsg, 'Hata');
      }

    });
  }


  onRoleChange(userId: number, event: Event) {
    const newRole = (event.target as HTMLSelectElement).value;
    this.userService.changeUserRole(userId, newRole).subscribe({
      next: () => {
        this.loadUsers();
        this.toastr.success('Rol başarıyla güncellendi');
      },
      error: (err: HttpErrorResponse) => {
        const errorMsg = err?.error?.message || 'Rol değiştirilemedi!';
        this.toastr.error(errorMsg);
        this.loadUsers(); // Rol değişmediği için arayüzü sıfırla
      }
    });
  }
}
