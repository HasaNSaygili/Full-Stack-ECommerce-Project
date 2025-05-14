import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FavoriteService } from '../../core/favorite.service';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-product-card',
  standalone:false,
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent {
  @Input() product: any;
  @Output() addToCart = new EventEmitter<any>();

  isFavorite = false;
  userId: number = 0;

  constructor(private favoriteService: FavoriteService, private authService: AuthService) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUser()?.id; // JWT içinden kullanıcıyı al
    this.checkFavoriteStatus();
  }

  ngOnChanges() {
    this.checkFavoriteStatus();
  }

  checkFavoriteStatus() {
    this.favoriteService.getFavorites(this.userId).subscribe(favorites => {
      this.isFavorite = favorites.some(fav => fav.id === this.product.id);
    });
  }

  toggleFavorite() {
    if (this.isFavorite) {
      this.favoriteService.removeFavorite(this.userId, this.product.id).subscribe(() => {
        this.isFavorite = false;
      });
    } else {
      this.favoriteService.addFavorite(this.userId, this.product.id).subscribe(() => {
        this.isFavorite = true;
      });
    }
  }

  onAddToCart() {
    this.addToCart.emit(this.product);
  }
}
