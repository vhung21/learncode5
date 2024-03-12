import { UserService } from 'app/_services/user.service';
import { User } from './../../models/User';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'app/_services/auth.service';
import { TokenService } from 'app/_services/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  alertMessage: string = '';
  role: any = 'user';
  users: any;
  usernameSearch: string = '';
  fullNameSearch: string = '';
  emailSearch: string = '';
  isLogin: boolean = this.authService.isLogin;
  updateUser: User = new User();
  userModal: any = new User();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    protected tokenService: TokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLogin = this.authService.isLogin;
    console.log(this.authService.isLogin);
    if (this.isLogin === false) {
      this.router.navigate(['/login']);
    }
    this.userService.getAllUser().subscribe((data: { data: any }) => {
      this.users = data.data;
      console.log(this.users);
    });
  }

  onUpdate(user: any) {
    this.userModal = user;
    console.log(this.tokenService.getRole());
  }

  // onModalSubmit() {
  //   console.log(this.tokenService.getRole());
  //   if (this.tokenService.getRole() !== 'ADMIN') {
  //     alert("you don't have permission to do this action");
  //     return;
  //   }
  //   var roles: string[] = [this.role];
  //   console.log(
  //     roles,
  //     this.userModal.id,
  //     this.updateUser.fullName,
  //     this.updateUser.email
  //   );
  //   this.userService
  //     .updateUser(
  //       this.userModal.id,
  //       this.updateUser.fullName,
  //       this.updateUser.email,
  //       roles
  //     )
  //     .subscribe(
  //       (data) => {
  //         alert('update successfully');
  //         this.userService.getAllUser().subscribe((data) => {
  //           this.users = data.data;
  //         });
  //         location.reload();
  //       },
  //       (error) => {
  //         this.alertMessage = error.error.message
  //           ? error.error.message
  //           : error.error.error;
  //         alert(this.alertMessage);
  //       }
  //     );
  // }

  onModalSubmit() {
    if (this.tokenService.getRole() === 'ADMIN') {
      this.AdminUpdate();
    } else if (this.tokenService.getRole() == 'USER' && this.userModal.username === this.tokenService.getUsername()) {
      this.UserUpdate();
    } else {
      alert("You don't have permission to do this action");
    }
  }
  private AdminUpdate() {
    var roles: string[] = [this.role];
    this.userService
      .updateUser(
        this.userModal.id,
        this.updateUser.fullName,
        this.updateUser.email,
        roles
      )
      .subscribe(
        (data) => {
          this.handleUpdateSuccess(data);
        },
        (error) => {
          this.handleUpdateError(error);
        }
      );
  }

  private UserUpdate() {
    var roles: string[] = [this.tokenService.getRole()];
    this.userService
      .updateUser(
        this.userModal.id,
        this.updateUser.fullName,
        this.updateUser.email,
        roles,
      )
      .subscribe(
        (data) => {
          this.handleUpdateSuccess(data);
        },
        (error) => {
          this.handleUpdateError(error);
        }
      );
  }

  private handleUpdateSuccess(data: any) {
    alert('Update successfully');
    this.userService.getAllUser().subscribe((data) => {
      this.users = data.data;
    });
    location.reload();
  }

  private handleUpdateError(error: any) {
    this.alertMessage = error.error.message
      ? error.error.message
      : error.error.error;
    alert(this.alertMessage);
  }

  delete(user: any) {
    if (user.username === this.tokenService.getUsername()) {
      alert("you can't do this action");
      return;
    }
    var id = user.id;
    if (confirm('Do you want to delete user ' + user.username + '?')) {
      this.userService.deleteUser(id).subscribe(
        (data) => {
          var listUser: User[] = <User[]>this.users;
          this.users = listUser.filter(function (user)
          {
            return user.id != id;
          }
          );
        },
        (error) => {
          alert("You don't have permission to do this action!");
        }
      );
    }
  }
}
