import { Component } from '@angular/core';
import { CurrentUserService } from '../current-user.service';
import { User } from '../user';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  title: string;

  constructor(private currentUserService: CurrentUserService) {
    this.title = "Home Component";
    this.displayUsername();
  }

  displayUsername() {
    this.currentUserService.getCurrentUser().subscribe((data: User) => {
      this.title = data.username
    });
  }
}
