package edu.pk.pj.pong.server;

import java.util.List;

import edu.pk.pj.pong.Entity;

public class CollisionHandler {

  public void handle(Ball ball, List<Player> players, Board board) 
      throws BallOfBoardOnLeftSide, BallOfBoardOnRightSide {
    final Entity.State ballState = ball.getState();
    
    for (Player player : players) {
      final Entity.State paddleState = player.getPaddle().getState();
      if (paddleState.y <= 0) {
        paddleState.y = 0;
      }
      if (paddleState.y + paddleState.height >= board.getHeight()) {
        paddleState.y = board.getHeight() - paddleState.height;
      }
      if (ballState.x <= paddleState.x + paddleState.width &&
          ballState.x + ballState.width >= paddleState.x) {
        if (ballState.y + ballState.height >= paddleState.y && 
            ballState.y <= paddleState.y + paddleState.height) {
          handleBallPlayerCollision(ball, player, board);
        }
      }
    }
    
    // Kolizja z gorna/dolna sciana
    if (ballState.y <= 0 || ballState.y >= board.getHeight() - ballState.height) {
      ballState.directionY = -ballState.directionY;
    }
    
    // Kolizja z bocznymi scianami
    if (ballState.x <= 0) {
      ballState.directionX = -ballState.directionX;
      throw new CollisionHandler.BallOfBoardOnLeftSide();
    }
    if (ballState.x >= board.getWidth() - ballState.width) {
      ballState.directionX = -ballState.directionX;
      throw new CollisionHandler.BallOfBoardOnRightSide();
    }
  }
  
  private void handleBallPlayerCollision(Ball ball, Player player, Board board) {
    final Entity.State ballState = ball.getState();
    final Entity.State paddleState = player.getPaddle().getState();
    boolean leftsidePlayer = board.getWidth() - paddleState.x > paddleState.x;
    
    ballState.directionX = -ballState.directionX;
    if (leftsidePlayer) {
      ballState.x = paddleState.x + paddleState.width;
    } else {
      ballState.x = paddleState.x - ballState.width;
    }
  }
  
  
  public static class BallOfBoardOnLeftSide extends Throwable {}
  
  public static class BallOfBoardOnRightSide extends Throwable {}
}
