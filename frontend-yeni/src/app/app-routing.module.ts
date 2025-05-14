import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { CartComponent } from './pages/cart/cart.component';
import { SellerComponent } from './pages/seller/seller.component';
import { AdminComponent } from './pages/admin/admin.component';
import { CheckoutComponent } from './pages/checkout/checkout.component';
import { AuthGuard } from './core/auth.guard';
import { ProfileComponent } from './pages/profile/profile.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { OrdersComponent } from './pages/orders/orders.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'favorites', component: FavoritesComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrdersComponent, canActivate: [AuthGuard] },
  { path: 'admin', loadChildren: () => import('./pages/admin/admin.module').then(m => m.AdminModule),canActivate: [AuthGuard],
    data: { role: 'ADMIN' } },
  { path: 'seller', loadChildren: () => import('./pages/seller/seller.module').then(m => m.SellerModule),
    canActivate: [AuthGuard],
    data: { role: 'SELLER' }
   },
  { path: 'cart', loadChildren: () => import('./pages/cart/cart.module').then(m => m.CartModule) }


];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
