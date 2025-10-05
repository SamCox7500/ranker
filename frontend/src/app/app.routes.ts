import { Routes } from '@angular/router';
import { UserListComponent } from './features/user-list/user-list.component';
import { UserFormComponent } from './features/user-form/user-form.component';
import { HomeComponent } from './features/home/home.component';
import { LoginFormComponent } from './features/login-form/login-form.component';
import { RegisterComponent } from './features/register/register.component';
import { ManageAccountComponent } from './features/manage-account/manage-account.component';
import { RankingListComponent } from './features/ranking-list/ranking-list.component';
import { LandingComponent } from './features/landing/landing.component';
import { NumberedRankingAddMediaComponent } from './features/numbered-ranking-add-media/numbered-ranking-add-media.component';
import { NumberedRankingComponent } from './features/numbered-ranking/numbered-ranking.component';
import { CreateNumberedRankingComponent } from './features/create-numbered-ranking/create-numbered-ranking.component';


export const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'createuser', component: UserFormComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'manageuser', component: ManageAccountComponent },
  { path: 'rankings', component: RankingListComponent },
  { path: '', component: LandingComponent},
  { path: 'numberedrankings/:rankingId', component: NumberedRankingComponent },
  { path: 'add-media/:rankingId/:addMediaRanking/:mediaType', component: NumberedRankingAddMediaComponent},
  { path: 'createnumranking', component: CreateNumberedRankingComponent}
];
