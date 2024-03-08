import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/form-login/login/login.component';
import { RegisterComponent } from './components/form-login/register/register.component';
import { HomeComponent } from './components/form-home/home.component';
import {ProductComponent} from "./components/form-product/product.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent, data: {title: 'login'}},
  {path: 'register', component:RegisterComponent, data :{title: 'Register'}},
  {path : 'home',component : HomeComponent},
  {path : 'product',component : ProductComponent},
  {path : '**',component: HomeComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
