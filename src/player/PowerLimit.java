package player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author NgocThach
 */

@Setter
@Getter
@AllArgsConstructor
@Builder
public class PowerLimit {

    private int id;
    private long power;
    private double hp;
    private double mp;
    private double damage;
    private int defense;
    private int critical;
}
