import {Injectable} from '@angular/core';
import {UserService} from "./user.service";
import { JwtHelperService } from '@auth0/angular-jwt';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const ROLE_KEY = 'auth-role';
const Name_Key = 'auth-name';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(private userService: UserService) {
  }

  getToken(): any {
    return window.localStorage.getItem(TOKEN_KEY);
  }

  saveToken(token: string): void {
    window.localStorage.clear();
    window.localStorage.setItem(TOKEN_KEY, token);
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(token);
    this.saveUser(decodedToken.sub);
    this.userService.getUser(decodedToken.sub).subscribe(data =>{
      this.saveRole(data.data.roles);
    })
  }

  logout(): void {
    window.localStorage.clear();
  }

  saveUser(username : any): void {
    window.localStorage.setItem(USER_KEY, username);
  }

  getUsername(): any {
    return window.localStorage.getItem(USER_KEY);
  }

  saveRole(role : any) {
    window.localStorage.setItem(ROLE_KEY,role);
  }
  getRole () : any {
    return window.localStorage.getItem(ROLE_KEY);
  }
}
