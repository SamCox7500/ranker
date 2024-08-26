import { Component } from '@angular/core';

@Component({
  selector: 'app-ranking-list',
  standalone: true,
  imports: [],
  templateUrl: './ranking-list.component.html',
  styleUrl: './ranking-list.component.css'
})
export class RankingListComponent {

  rankings: Ranking[] = [];

  constructor(private userService: UserService) {
    userService.findAll().subscribe((data: User[]) => {
      this.users = data;
    });
  }

}
