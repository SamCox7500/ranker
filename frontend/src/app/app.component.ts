import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'
import { RouterLink } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { RouterLinkActive } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CurrentUserService } from './services/current-user.service';
import { User } from './core/models/user';
import { LoginService } from './services/login.service';
import { SharedRankingService } from './services/shared-ranking.service';
import { response } from 'express';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  user: User | null = null;
  authenticated: boolean = false;

  shareToken = new FormControl('');

  constructor(private loginService: LoginService, private currentUserService: CurrentUserService, private router: Router, private sharedRankingService: SharedRankingService) {}

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
  searchByToken(token: string): void {
    console.log(token);
    this.sharedRankingService.lookupSharedRanking(token).subscribe({
      next: (response) => {
        if (response.rankingType === 'NUMBERED_RANKING') {
          this.router.navigate(['/shared/numberedrankings', token]);
        } else if (response.rankingType === 'TIER_LIST') {
          //todo
        } else {
          alert('Unknown ranking type');
        }
      },
      error: () => {
        alert('Invalid share token');
      }
    });
  }
}
