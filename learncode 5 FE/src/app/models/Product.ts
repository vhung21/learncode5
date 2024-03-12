export class Product{
  id !: number;
  productName !: string;
  productCode !: string;
  manufacturer !: string;
  quantity !: number;
  price !: number;
  addedBy !: string;
  category !: { id: any };
}
