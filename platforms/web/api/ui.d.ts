export interface UIMethods {
  /** makes fades in the screen
   * @returns nothing
   */
  fadeIn(): Promise<void>;
  /** makes fades out the screen
   * @returns nothing
   */
  fadeOut(): Promise<void>;
}
