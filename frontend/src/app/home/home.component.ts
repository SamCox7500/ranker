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

  user: User | null = null;

  constructor(private currentUserService: CurrentUserService, private router: Router, private loginService: LoginService) {
  }
  ngOnInit(): void {
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (!user) {
        this.currentUserService.fetchCurrentUser().subscribe();
      }
    });
  }
  logout() {
    this.loginService.logout().subscribe({
      next: () => this.goToLoginForm(),
      error: err => console.error('Logout failed', err)
    });
  }
  goToLoginForm() {
    this.router.navigate(['/login']);
  }
  goToManageAccount() {
    this.router.navigate(['/manageuser']);
  }
  goToRankings() {
    this.router.navigate(['/rankings'])
  }
}
