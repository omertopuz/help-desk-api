import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserInfo } from './_models/entity/user-info';
import { AuthService } from './_services/auth.service';
import { first } from 'rxjs/operators';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'help-desk-ui';
  currentUser: UserInfo;

  returnUrl: string;
  error = '';

  constructor( 
    private router: Router,
    private authService: AuthService
  ){
      this.authService.currentUser.subscribe(x => this.currentUser = x);
  }


  loginUser() {

    const username : string = "user-1";
    const password : string = "password";
    this.returnUrl = "/";
    
    this.authService.login(username, password)
        .pipe(first())
        .subscribe(
            data => {
                this.router.navigate([this.returnUrl]);
            },
            error => {
                this.error = error;
            });
}

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
}

}
