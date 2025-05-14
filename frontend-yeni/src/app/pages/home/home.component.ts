import { Component, OnInit } from '@angular/core';
import { CartService } from '../../core/cart.service';
import { FavoriteService } from '../../core/favorite.service';
import { AuthService } from '../../core/auth.service';
import { ProductService } from '../../core/product.service';
import { CategoryService } from '../../core/category.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: any[] = [];
  searchText = '';
  selectedCategory = 'Tümü';
  sortOrder = 'varsayılan';
  favoriteProductIds: number[] = [];
  userId?: number;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private cartService: CartService,
    private favoriteService: FavoriteService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user?.id) {
      this.userId = user.id;
      this.loadFavorites(this.userId!);
    }
    this.loadProducts();
    this.loadCategories();
  }


  loadProducts() {
    this.productService.getProducts().subscribe(data => {
      this.products = data;
    });
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe(data => {
      this.categories = data;
    });
  }

  loadFavorites(userId: number) {
    this.favoriteService.getFavorites(userId).subscribe(data => {
      this.favoriteProductIds = data.map(fav => fav.productId); // Eğer sadece id döndürüyorsa
    });
  }

  isFavorite(productId: number): boolean {
    return this.favoriteProductIds.includes(productId);
  }

  toggleFavorite(product: Product) {
    if (!this.userId) return;

    if (this.isFavorite(product.id)) {
      this.favoriteService.removeFavorite(this.userId, product.id).subscribe(() => {
        this.favoriteProductIds = this.favoriteProductIds.filter(id => id !== product.id);
      });
    } else {
      this.favoriteService.addFavorite(this.userId, product.id).subscribe(() => {
        this.favoriteProductIds.push(product.id);
      });
    }
  }


  addToCart(product: Product) {
    if (this.userId) {
      this.cartService.addToCart(this.userId, product.id, 1).subscribe(() => {
        alert(product.name + ' sepete eklendi!');
      });
    }
  }

  get filteredProducts() {
    let result = [...this.products];

    if (this.searchText) {
      result = result.filter(product =>
        product.name.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }

    if (this.selectedCategory !== 'Tümü') {
      result = result.filter(
        product => product.categoryName === this.selectedCategory
      );
    }

    result.sort((a, b) => (b.stock > 0 ? 1 : -1) - (a.stock > 0 ? 1 : -1));

    if (this.sortOrder === 'artan') {
      result.sort((a, b) => a.price - b.price);
    } else if (this.sortOrder === 'azalan') {
      result.sort((a, b) => b.price - a.price);
    }

    return result;
  }
}
