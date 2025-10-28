import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';

import { numberedRankingResolver } from './numbered-ranking.resolver';

describe('numberedRankingResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => 
      TestBed.runInInjectionContext(() => numberedRankingResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
