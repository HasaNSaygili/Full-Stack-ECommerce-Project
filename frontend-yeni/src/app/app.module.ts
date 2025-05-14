import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { CartComponent } from './pages/cart/cart.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { SellerComponent } from './pages/seller/seller.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminComponent } from './pages/admin/admin.component';
import { CheckoutComponent } from './pages/checkout/checkout.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { SpinnerComponent } from './shared/spinner/spinner.component';
import { PricePipe } from './shared/price/price.pipe';
import { ErrorInterceptor } from './core/error.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/auth.interceptor';
import { ProductCardComponent } from './pages/product-card/product-card.component';
import { HttpClientModule } from '@angular/common/http';
import { NgxStripeModule } from 'ngx-stripe';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    ProductDetailComponent,
    NavbarComponent,
    CheckoutComponent,
    ProfileComponent,
    FavoritesComponent,
    OrdersComponent,
    SpinnerComponent,
    ProductCardComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule,
    PricePipe,
    HttpClientModule,
    NgxStripeModule.forRoot('pk_test_51RLQB2GbMbgKbqVAU3iK2wRCmHyK7SYCN9NqY7ckLXkmA2JEK4aXolfPSry72GVgNuguhMkxU3BlNxD654qVN8qR004Qa6XPFy'),
  ],
  providers: [
    provideClientHydration(withEventReplay()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
