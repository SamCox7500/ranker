import { TestBed } from '@angular/core/testing';

import { SharedRankingService } from './shared-ranking.service';

describe('SharedRankingService', () => {
  let service: SharedRankingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedRankingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
