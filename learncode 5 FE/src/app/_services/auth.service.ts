import { Injectable } from '@angular/core';
import { SignIn } from '../models/SignIn';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SignUp } from '../models/SignUp';

const AUTH_API = 'http://localhost:8080/api/auth/';
const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLogin: boolean=false;
  constructor(private http: HttpClient) {
    if (window.localStorage.getItem(TOKEN_KEY)) {
      this.isLogin = true;
   }
   else {
    this.isLogin=false;
   }
  }

  login(signInRequest : SignIn) : Observable<any>{
    return this.http.post(AUTH_API+ 'login',signInRequest);
  }
  register(signUpRequest : SignUp): Observable<any> {
    return this.http.post(AUTH_API + 'registration', signUpRequest);
  }
  logOut() :void{
    window.localStorage.clear();
    this.isLogin=false;
  }
  test (): Observable<any>{
    return this.http.get('http://localhost:8080/users')
}
}