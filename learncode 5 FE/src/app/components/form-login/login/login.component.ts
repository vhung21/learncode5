import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import { SignIn } from 'app/models/SignIn';
import { AuthService } from '../../../_services/auth.service';
import { UserService } from '../../../_services/user.service';
import { TokenService } from '../../../_services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  signIn: SignIn = new SignIn();
  isSuccessful: boolean = true;
  errorMessage: string ="";

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router
  ) {
  }
  ngOnInit():void{
    if (this.authService.isLogin){
      this.router.navigate(['home'])
    }
  }

  onSubmit(): void {
    this.authService.login(this.signIn).subscribe(data => {
      this.tokenService.saveToken(data.data.token);
      this.authService.isLogin = true;
      this.router.navigate(['/home']);
      },
      error => {
        this.isSuccessful = false;
         this.errorMessage=error.error.message? error.error.message : "Password was wrong";
      })
  }

  test(): void {
    this.userService.getAllUser().subscribe((data: any) => {
      console.log(data)
    })
  }


}
