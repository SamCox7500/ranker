import { Component } from '@angular/core';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';
import { UserService } from '../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { OnDestroy } from '@angular/core';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-manage-account',
  standalone: true,
  imports: [],
  templateUrl: './manage-account.component.html',
  styleUrl: './manage-account.component.css'
})
export class ManageAccountComponent {

  user: User | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(private userService: UserService, private currentUserService: CurrentUserService, private router: Router, private loginService : LoginService) {

  }

  ngOnInit(): void {
    const getCurrentUserSub = this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (!user) {
        const fetchUserSub = this.currentUserService.fetchCurrentUser().subscribe();
        this.subscriptions.add(fetchUserSub);
      }
    });
    this.subscriptions.add(getCurrentUserSub);
  }

  deleteAccount() {
    if(confirm("Are you sure you want to delete your account? You will not be able to recover it and ALL YOUR RANKINGS WILL BE LOST.")) {
      if (this.user) {
        const deleteUserSub = this.userService.deleteUser(this.user.id).subscribe({
          next: () => this.logout(),
          error: err => console.error('Failed to delete account', err)
        });
        this.subscriptions.add(deleteUserSub);
      }
    }
  }
  logout() {
    const logoutSub = this.loginService.logout().subscribe({
      next: () => this.goToLogin(),
      error: err => console.error("Could not log out after deleting account", err)
    });
    this.subscriptions.add(logoutSub);
  }
  goToLogin() {
    this.router.navigate(['/login']);
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
