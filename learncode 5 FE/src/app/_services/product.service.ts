import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import { environment } from 'environments/environments';
import {Product} from "../models/Product";

const API = 'http://localhost:8080/product'

@Injectable({
  providedIn: 'root'
})

export class ProductService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient,
  ) {

  }

  getProductList(): Observable<any> {
    return this.http.get(API);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(API + "/" + id);
  }

  get(product_name: any): Observable<any> {
    return this.http.get(API + "/" + product_name);
  }

  update(id: number, productName: string, productCode: string, category: {
    id: any
  }, manufacturer: string, quantity: number, price: number): Observable<any> {
    var product: Product = new Product();
    product.productName= productName;
    product.productCode= productCode;
    product.category = category;
    product.manufacturer= manufacturer;
    product.quantity= quantity;
    product.price= price;
    return this.http.post(API + "/" + id, product);
  }

  add(id: number, productName: string, productCode: string, category: {
    id: any
  }, manufacturer: string, quantity: number, price: number, addedBy: string): Observable<any> {
    var product: Product = new Product();
    product.productName= productName;
    product.productCode= productCode;
    product.category = category;
    product.manufacturer= manufacturer;
    product.quantity= quantity;
    product.price= price;
    product.addedBy = addedBy;
    return this.http.post(API + '/add',product);
  }
}
