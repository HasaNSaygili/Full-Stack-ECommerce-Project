import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../core/product.service';
import { AuthService } from '../../core/auth.service';
import { CategoryService } from '../../core/category.service';
import { HttpHeaders } from '@angular/common/http';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-seller',
  standalone: false,
  templateUrl: './seller.component.html',
  styleUrls: ['./seller.component.css']
})
export class SellerComponent implements OnInit {
  productForm!: FormGroup;
  products: any[] = [];
  categories: any[] = [];
  editingProduct: any = null;
  sellerId!: number;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private authService: AuthService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    this.sellerId = currentUser?.id;

    this.productForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      imageUrl: ['', Validators.required],
      categoryId: [null, Validators.required],
      categoryName: ['', Validators.required],
      sellerId: [this.sellerId]
    });

    this.loadProducts();
    this.loadCategories();
  }

  loadProducts() {
    this.productService.getMyProducts().subscribe({
      next: data => this.products = data,
      error: err => console.error('Ürünler yüklenemedi', err)
    });
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: data => this.categories = data,
      error: err => console.error('Kategoriler yüklenemedi', err)
    });
  }

  submit() {
    if (this.productForm.valid) {
      this.productForm.patchValue({ sellerId: this.sellerId });

      if (this.editingProduct) {
        this.updateProduct();
      } else {
        this.addProduct();
      }
    }
  }

  addProduct() {
    this.productService.addProduct(this.productForm.value).subscribe({
      next: () => {
        this.loadProducts();
        this.productForm.reset();
      },
      error: err => console.error('Ürün eklenemedi', err)
    });
  }

  editProduct(product: any) {
    this.editingProduct = product;
    this.productForm.patchValue(product);
  }

  updateProduct() {
    this.productService.updateProduct(this.productForm.value).subscribe({
      next: () => {
        this.loadProducts();
        this.editingProduct = null;
        this.productForm.reset();
      },
      error: err => console.error('Güncelleme hatası', err)
    });
  }

  deleteProduct(id: number) {
    if (confirm('Bu ürünü silmek istediğinize emin misiniz?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts();
        },
        error: err => {
          console.error('Silme hatası', err);
          alert('Ürün silinemedi.');
        }
      });
    }
  }

  onCategoryChange(event: Event) {
    const categoryId = +(event.target as HTMLSelectElement).value;
    const category = this.categories.find(c => c.id === categoryId);
    if (category) {
      this.productForm.patchValue({ categoryName: category.name });
    }
  }

}
