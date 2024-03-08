import {Component} from '@angular/core';
import { AuthService } from 'app/_services/auth.service';
import { SignUp } from 'app/models/SignUp';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  isSuccessful : boolean=true;
  errorMessage : string="";
  successMessage : string ="";
  signUp: SignUp =new SignUp();
  constructor(private authService : AuthService) {
  }
  onSubmit(): void {
    console.log(this.signUp);
    this.signUp.roles=[];
    this.signUp.addRole("user");
    this.authService.register(this.signUp).subscribe(data =>{
      console.log(data);
      this.successMessage=data.message;
      this.isSuccessful=true;
    }, error => {
      this.isSuccessful=false;
      this.errorMessage=error.error.message? error.error.message : error.error.error;
    })
  }
}
