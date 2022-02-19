package com.pocket.currencies.pocket;

import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.ProfitDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/pocket")
public class PocketController {

    private final PocketService pocketService;

    @PostMapping(value = "/deposit/add")
    @ResponseStatus(HttpStatus.OK)
    public String addDeposit(@RequestBody DepositDto depositDto) {
        return pocketService.addDeposit(depositDto);
    }

    @GetMapping(value = "/deposit/remove/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String removeDeposit(@PathVariable long id) {
        return pocketService.removeDeposit(id);
    }

    @GetMapping(value = "/profit")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ProfitDto calculateProfit() {
        return pocketService.calculateProfit();
    }

    @GetMapping(value = "/deposit/{page}/{size}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Deposit> getDeposits(@PathVariable int page, @PathVariable int size) {
        return pocketService.getAllDepositsForCurrentUser(page, size);
    }

    @GetMapping(value = "/deposit/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAmountOfDeposits() {
        return pocketService.getAmountOfDepositsForCurrentUser();
    }
}
