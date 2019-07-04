.file "tests/codeGenOfMe/outOfControl_01.dcf"
.text
.text
.globl func
.type func, @function
func:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq %rdi,-8(%rbp)
movq -8(%rbp),%rax
cmpq $4,%rax
jle .L5
movq $4,%rax
jmp .L3
jmp .L3
.L5:
movq -8(%rbp),%rax
cmpq $20,%rax
jge .L6
movq $20,%rax
jmp .L3
jmp .L3
.L6:
movq $100,%rax
jmp .L3
.L3:
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
.L4:
leave
ret

