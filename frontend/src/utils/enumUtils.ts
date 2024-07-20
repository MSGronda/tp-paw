export function timeDemandToText(timeDemand: number, t: (key: string) => string) {
  switch (timeDemand) {
    case 0:
      return t("Curriculum.low")
    case 1:
      return t("Curriculum.medium")
    case 2:
      return t("Curriculum.high")
  }
}

export function difficultyToText(difficulty: number, t: (key: string) => string) {
  switch (difficulty) {
    case 0:
      return t("Curriculum.easy")
    case 1:
      return t("Curriculum.medium")
    case 2:
      return t("Curriculum.hard")
  }
}
