import { Component } from '@angular/core';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';
import { UserService } from '../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-manage-account',
  standalone: true,
  imports: [],
  templateUrl: './manage-account.component.html',
  styleUrl: './manage-account.component.css'
})
export class ManageAccountComponent {

  user: User | null = null;

  constructor(private userService: UserService, private currentUserService: CurrentUserService, private router: Router) {

  }

  ngOnInit(): void {
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (!user) {
        this.currentUserService.fetchCurrentUser().subscribe();
      }
    });
  }

  deleteAccount() {
    if(confirm("Are you sure you want to delete your account? You will not be able to recover it and ALL YOUR RANKINGS WILL BE LOST.")) {
      if (this.user) {
        this.userService.deleteUser(this.user.id).subscribe({
          next: () => this.goToLogin(),
          error: err => console.error('Failed to delete account', err)
        });
      }
    }
  }
  goToLogin() {
    this.router.navigate(['/login']);
  }
}
