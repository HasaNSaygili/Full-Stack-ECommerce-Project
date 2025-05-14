import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  standalone:false,
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  productId!: number;
  product: any;

  products = [
    {
      id: 1,
      name: 'Bluetooth Kulaklık',
      price: 349,
      oldPrice: 499,
      stock: 15,
      description: 'Yüksek kaliteli ses deneyimi sağlar.',
      image: 'https://via.placeholder.com/400'
    },
    {
      id: 2,
      name: 'Laptop Çantası',
      price: 159,
      oldPrice: 199,
      stock: 5,
      description: 'Dayanıklı ve su geçirmez laptop çantası.',
      image: 'https://via.placeholder.com/400'
    },
    {
      id: 3,
      name: 'Kablosuz Mouse',
      price: 129,
      oldPrice: 189,
      stock: 0,
      description: 'Ergonomik tasarımlı kablosuz mouse.',
      image: 'https://via.placeholder.com/400'
    },
    {
      id: 4,
      name: 'Gaming Klavye',
      price: 449,
      oldPrice: 599,
      stock: 8,
      description: 'RGB aydınlatmalı mekanik gaming klavye.',
      image: 'https://via.placeholder.com/400'
    }
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.product = this.products.find(p => p.id === this.productId);
  }
}
