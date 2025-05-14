import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../core/product.service';

@Component({
  selector: 'app-admin-product',
  standalone: false,
  templateUrl: './admin-product.component.html',
  styleUrls: ['./admin-product.component.css']
})
export class AdminProductComponent {
  productForm: FormGroup;
  products: any[] = [];
  editingProduct: any = null;

  constructor(private fb: FormBuilder, private productService: ProductService) {
    this.productForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, Validators.required],
      imageUrl: ['', Validators.required],
      categoryId: ['', Validators.required],
      sellerId: [1] // örnek seller id
    });

    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe(data => {
      this.products = data;
    });
  }

  submit() {
    if (this.productForm.valid) {
      if (this.editingProduct) {
        this.updateProduct();
      } else {
        this.addProduct();
      }
    }
  }

  addProduct() {
    const newProduct = this.productForm.value;
    this.productService.addProduct(newProduct).subscribe(() => {
      this.loadProducts();
      this.productForm.reset();
    });
  }

  deleteProduct(id: number) {
    this.productService.deleteProduct(id).subscribe(() => {
      this.loadProducts();
    });
  }

  editProduct(product: any) {
    this.editingProduct = product;
    this.productForm.patchValue(product);
  }

  updateProduct() {
    const updatedProduct = this.productForm.value;
    this.productService.updateProduct(updatedProduct).subscribe(() => {
      this.loadProducts();
      this.editingProduct = null;
      this.productForm.reset();
    });
  }
}
