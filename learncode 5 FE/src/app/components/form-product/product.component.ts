import { Component } from '@angular/core';
import {AuthService} from "../../_services/auth.service";
import {TokenService} from "../../_services/token.service";
import {Router} from "@angular/router";
import {Product} from "../../models/Product";
import {ProductService} from "../../_services/product.service";
import {User} from "../../models/User";

@Component({
  selector: 'product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent {
  productNameSearch: string = '';
  productCodeSearch: string = '';
  categorySearch: string = '';
  manufacturerSearch: string = '';
  quantitySearch: string = '';
  priceSearch: string = '';
  isLogin: boolean = this.authService.isLogin;
  products : any;
  category : any;
  productModal: any = new Product();
  updateProduct: Product = new Product();
  addProduct: Product = new Product();
  alertMessage: string = '';


  constructor(
    private productService: ProductService,
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) {}


  ngOnInit(): void {
    this.isLogin = this.authService.isLogin;
    console.log(this.authService.isLogin);
    if (this.isLogin === false) {
      this.router.navigate(['/login']);
    }
    this.productService.getProductList().subscribe((data: { data: any }) => {
      this.products = data.data;
      console.log(this.products);
    });
  }

  onUpdate(product: any) {
    this.productModal = product;
  }

  delete(product: any) {
    var id = product.id;
    if (confirm('Do you want to delete product ' + product.productName + '?')) {
      this.productService.delete(id).subscribe(
        (data) => {
          var listProduct: Product[] = <Product[]>this.products;
          this.products = listProduct.filter(function (product) {
            return product.id != id;
          });
        },
        (error) => {
          alert("You don't have permission to do this action!");
        }
      );
    }
  }

  onUpdateModalSubmit() {
    console.log(this.tokenService.getRole());
    if (this.tokenService.getRole() !== 'ADMIN') {
      alert("you don't have permission to do this action");
      return;
    }
    var categories: { id: any } = {"id": this.category };
    console.log(
      this.productModal.id,
      this.updateProduct.productName,
      this.updateProduct.productCode,
      this.updateProduct.manufacturer,
      this.updateProduct.quantity,
      this.updateProduct.price,
      categories
    );
    this.productService
      .update(
        this.productModal.id,
        this.updateProduct.productName,
        this.updateProduct.productCode,
        categories,
        this.updateProduct.manufacturer,
        this.updateProduct.quantity,
        this.updateProduct.price,
      )
      .subscribe(
        (data) => {
          alert('update successfully');
          this.productService.getProductList().subscribe((data) => {
            this.products = data.data;
          });
        },
        (error) => {
          this.alertMessage = error.error.message
            ? error.error.message
            : error.error.error;
          alert(this.alertMessage);
        }
      );
  }

  onAdd(product: any) {
    this.productModal = product;
  }

  onAddModalSubmit() {
    console.log(this.tokenService.getRole());
    if (this.tokenService.getRole() !== 'ADMIN') {
      alert("you don't have permission to do this action");
      return;
    }
    var categories: { id: any } = {"id": this.category };
    console.log(
      this.productModal.id,
      this.addProduct.productName,
      this.addProduct.productCode,
      this.addProduct.manufacturer,
      this.addProduct.quantity,
      this.addProduct.price,
      categories
    );
    this.productService
      .add(
        this.productModal.id,
        this.addProduct.productName,
        this.addProduct.productCode,
        categories,
        this.addProduct.manufacturer,
        this.addProduct.quantity,
        this.addProduct.price,
      )
      .subscribe(
        (data) => {
          alert('Add product successfully');
          this.productService.getProductList().subscribe((data) => {
            this.products = data.data;
          });
        },
        (error) => {
          this.alertMessage = error.error.message
            ? error.error.message
            : error.error.error;
          alert(this.alertMessage);
        }
      );
  }

}
