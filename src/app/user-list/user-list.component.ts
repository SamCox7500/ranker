import { Component } from '@angular/core';
import { User } from '../user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {

  users: User[] = [];

  constructor(private userService: UserService) {
    userService.findAll().subscribe((data: User[]) => {
      this.users = data;
    });
  }





}
