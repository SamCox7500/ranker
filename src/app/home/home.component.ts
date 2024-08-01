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

  title: string;

  constructor(private currentUserService: CurrentUserService, private router: Router, private loginService: LoginService) {
    this.title = "Home Component";
    this.displayUsername();
  }

  logout() {
    this.loginService.logout().subscribe({
      next: () => this.goToLoginForm(),
      error: err => console.error('Logout failed', err)
    });
  }
  displayUsername() {
    this.currentUserService.getCurrentUser().subscribe((data: User) => {
      this.title = data.username
    });
  }
  goToLoginForm() {
    this.router.navigate(['/login']);
  }
}
