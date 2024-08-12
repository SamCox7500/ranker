import { Component } from '@angular/core';
import { CurrentUserService } from '../services/current-user.service';
import { LoginService } from '../services/login.service';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  user: User = { id: 0, username: ''}

  constructor(private currentUserService: CurrentUserService, private router: Router, private loginService: LoginService) {
    this.getCurrentUser();
  }
  logout() {
    this.loginService.logout().subscribe({
      next: () => this.goToLoginForm(),
      error: err => console.error('Logout failed', err)
    });
  }
  getCurrentUser() {
    this.currentUserService.getCurrentUser().subscribe((user: User) => {
      this.user = user;
    });
  }
  goToLoginForm() {
    this.router.navigate(['/login']);
  }
}
