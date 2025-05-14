export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  stock: number;
  imageUrl: string;
  sellerId: number;
  categoryId: number;
  categoryName: string;
  oldPrice?: number;
}
