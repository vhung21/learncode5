import { Component } from '@angular/core';
import {Router} from "@angular/router";
import { AuthService } from './_services/auth.service';
import { TokenService } from './_services/token.service';
const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const ROLE_KEY = 'auth-role';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  infoCurrentUser: string = "";
  isLogin:boolean =false;
  constructor(private auService: AuthService,
              private router:Router,
              private tokenService : TokenService) {
    this.isLogin=auService.isLogin;

  }
  
  ngDoCheck(): void {
    this.isLogin=this.auService.isLogin;
    if (this.isLogin){
      this.infoCurrentUser=this.tokenService.getUsername() ;
    }
  }
  
  logOut(): void {
    this.auService.logOut();
    this.router.navigate(['login'])
    console.log(this.isLogin);
  }
  title = 'frontend';

}
