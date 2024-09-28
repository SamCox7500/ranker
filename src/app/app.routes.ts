import { Routes } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { HomeComponent } from './home/home.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { RegisterComponent } from './register/register.component';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { RankingListComponent } from './ranking-list/ranking-list.component';
import { LandingComponent } from './landing/landing.component';
import { CreateRankingComponent } from './create-ranking/create-ranking.component';
import { MediaListComponent } from './media-list/media-list.component';


export const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'createuser', component: UserFormComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'manageuser', component: ManageAccountComponent },
  { path: 'rankings', component: RankingListComponent },
  { path: '', component: LandingComponent},
  { path: 'createranking', component: CreateRankingComponent},
  { path: 'medialist/:rankingId', component: MediaListComponent},
];
