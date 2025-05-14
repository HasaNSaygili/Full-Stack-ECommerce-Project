import { Component } from '@angular/core';
import { FavoriteService } from '../../core/favorite.service';
import { AuthService } from '../../core/auth.service';
import { CartService } from '../../core/cart.service';

@Component({
  selector: 'app-favorites',
  standalone:false,
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent {
  favoriteItems: any[] = [];
  userId!: number;

  constructor(
    private favoriteService: FavoriteService,
    private authService: AuthService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
      this.loadFavorites();
    }
  }

  loadFavorites() {
    this.favoriteService.getFavorites(this.userId).subscribe(items => {
      this.favoriteItems = items;
    });
  }

  removeFromFavorites(productId: number) {
    console.log('Favoriden çıkarılacak ürün ID:', productId);
    this.favoriteService.removeFavorite(this.userId, productId).subscribe(() => {
      this.loadFavorites();
    });
  }

  addToCart(product: any) {
    this.cartService.addToCart(this.userId, product.id, 1).subscribe(() => {
      alert(`${product.name} sepete eklendi.`);
    });
  }
}
