
export interface Product {
  id: string;
  name: string;
  price: string;
  features: string[];
  imageUrl: string;
  category: string;
  description: string;
  priceRaw?: number; // Raw price in cents for purchase processing
}
