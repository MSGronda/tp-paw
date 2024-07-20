export interface SearchParams {
  q?: string,
  page?: number,
  minCredits?: number,
  maxCredits?: number,
  minDifficulty?: number,
  maxDifficulty?: number,
  minTimeDemand?: number,
  maxTimeDemand?: number,
  department?: string,
  orderBy?: string,
  degree?: number,
  dir?: "asc" | "desc",

  hasFilters(): boolean
}

export function parseSearchParams(params: URLSearchParams): SearchParams {
  const dir = params.get('dir');
  return {
    q: params.get('q') ?? undefined,
    page: params.has('page') ? parseInt(params.get('page')!) : 1,
    minCredits: params.has('minCredits') ? parseInt(params.get('minCredits')!) : undefined,
    maxCredits: params.has('maxCredits') ? parseInt(params.get('maxCredits')!) : undefined,
    minDifficulty: params.has('minDifficulty') ? parseInt(params.get('minDifficulty')!) : undefined,
    maxDifficulty: params.has('maxDifficulty') ? parseInt(params.get('maxDifficulty')!) : undefined,
    minTimeDemand: params.has('minTimeDemand') ? parseInt(params.get('minTimeDemand')!) : undefined,
    maxTimeDemand: params.has('maxTimeDemand') ? parseInt(params.get('maxTimeDemand')!) : undefined,
    department: params.has('department') ? params.get('department')! : undefined,
    orderBy: params.has('ob') ? params.get('ob')! : undefined,
    degree: params.has('degree') ? parseInt(params.get('degree')!) : undefined,
    dir: dir && ["asc", "desc"].includes(dir) ? dir as "asc" | "desc" : undefined,

    hasFilters(): boolean {
      return this.minCredits !== undefined 
        || this.maxCredits !== undefined 
        || this.minDifficulty !== undefined
        || this.maxDifficulty !== undefined 
        || this.minTimeDemand !== undefined
        || this.maxTimeDemand !== undefined
        || this.department !== undefined
        || this.orderBy !== undefined;
    }
  }
}
