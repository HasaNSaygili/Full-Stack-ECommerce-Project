import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private toastr: ToastrService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          // Client-Side Error
          this.toastr.error('Bir hata oluştu: ' + error.error.message, 'Hata');
        } else {
          // Server-Side Error
          switch (error.status) {
            case 0:
              this.toastr.error('Sunucuya ulaşılamıyor.', 'Bağlantı Hatası');
              break;
            case 400:
              this.toastr.error('Geçersiz istek.', 'Hata 400');
              break;
            case 401:
              this.toastr.error('Giriş yapmanız gerekiyor.', 'Yetkisiz Erişim');
              break;
            case 404:
              this.toastr.error('İstek bulunamadı.', 'Hata 404');
              break;
            case 500:
              this.toastr.error('Sunucu hatası.', 'Hata 500');
              break;
            default:
              this.toastr.error('Bilinmeyen bir hata oluştu.', 'Hata');
              break;
          }
        }
        return throwError(() => error);
      })
    );
  }
}
