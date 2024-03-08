export class SignUp {
    username !:string;
    password !:string;
    fullName !: string;
    email !: string;
    roles : string[]= [];
    public addRole (role : string) : void {
      this.roles.push(role)
    }
  
  
  }