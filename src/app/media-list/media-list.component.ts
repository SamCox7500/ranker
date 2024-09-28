import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MediaListService } from '../services/media-list.service';

@Component({
  selector: 'app-media-list',
  standalone: true,
  imports: [],
  templateUrl: './media-list.component.html',
  styleUrl: './media-list.component.css'
})
export class MediaListComponent implements OnInit {

  rankingId: number | null = null;

  constructor(private route: ActivatedRoute, private mediaListService: MediaListService) {}

  ngOnInit(): void {
      this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
  }
}
