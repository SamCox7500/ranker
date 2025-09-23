import { TestBed } from '@angular/core/testing';

import { NumberedRankingService } from './numbered-ranking.service';

describe('NumberedRankingService', () => {
  let service: NumberedRankingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NumberedRankingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
