import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'
import { RouterLink } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { RouterLinkActive } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CurrentUserService } from './services/current-user.service';
import { User } from './core/models/user';
import { LoginService } from './services/login.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  user: User | null = null;
  authenticated: boolean = false;

  constructor(private loginService: LoginService, private currentUserService: CurrentUserService, private router: Router) {}

  ngOnInit(): void {
    this.updateAuth();
  }
  logout() {
    this.loginService.logout().subscribe({
      next: () => {
        this.updateAuth();
        this.goToLanding();
      },
      error: err => console.error('Logout failed', err)
    });
  }
  updateAuth() {
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
    });
    this.loginService.authenticated$.subscribe(authStatus => {
      this.authenticated = authStatus;
    }); 
  }
  goToLanding() {
    this.router.navigate(['/']);
  }
}
