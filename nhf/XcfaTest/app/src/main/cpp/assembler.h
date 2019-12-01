//
// Created by levente on 12/1/19.
//

#ifndef XCFATEST_ASSEMBLER_H
#define XCFATEST_ASSEMBLER_H

#include "aarch64/macro-assembler-aarch64.h"
#include "aarch64/simulator-aarch64.h"
#include <map>

using namespace vixl::aarch64;

class MAssembler
{
private:
    MacroAssembler* masm;
    Decoder* decoder;
    Simulator* simulator;
    Label* firstLabel = nullptr;
public:
    MAssembler();

    void run();

    int64_t getReg(unsigned int i);

    void bind(Label* label);

    MacroAssembler* getMasm();

    ~MAssembler();

    void addGlobal(unsigned int reg, uint64_t value);
};

#endif //XCFATEST_ASSEMBLER_H
